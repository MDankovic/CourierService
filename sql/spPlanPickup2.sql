use KurirskaSluzba
go

drop procedure if exists spPlanPickup2
go

create procedure spPlanPickup2
	@courier varchar(100)
as
begin
	-- courier info
	declare @city int
	declare @courierAddress int
	declare @currentAddress int
	declare @reg varchar(100)

	-- package info
	declare @cursorHome cursor
	declare @cursorStorage cursor
	declare @idP int
	declare @weight decimal(10,3)
	declare @capacity decimal(10,3)
	declare @loaded decimal(10,3)
	declare @status int
	declare @toDeliver int
	declare @tmpStr varchar(100)

	-- operation success
	declare @success int = 0

	-- get courier location
	select @currentAddress = TrenutnaLokacija, @reg = RegistracioniBroj from KURIR where KorisnickoIme = @courier
		
	-- get city
	select @city = IdGrad, @courierAddress = Id from ADRESA where Id = @currentAddress
	print('kurir trenutno:' + cast(@currentAddress as varchar) + ':' + + cast(@city as varchar))
	
	
	-- get car capacity and current load
	select @capacity = Nosivost, @loaded = Zauzeto from VOZILO where RegistracioniBroj = @reg
	
	-- get all possible packages from users
	set @cursorHome = cursor for
	select Id, Tezina
	from PAKET
	where (
		Status = 1 
		and 
		(select IdGrad from ADRESA where Id = PAKET.AdresaOd) = @city
		and 
		PAKET.Id not in (select IdPaket from PLAN_VOZNJE)
		
	)
	order by VremePrihvatanja ASC

	open @cursorHome

	fetch next from @cursorHome
	into @idP, @weight

	-- plan what packages to take until it reaches car maximum capacity (greedy order by time)
	while @@FETCH_STATUS = 0
	begin
		if(@loaded + @weight <= @capacity)
		begin
			set @loaded = @loaded + @weight

			insert into PLAN_VOZNJE (IdPaket, Kurir, ZaDostavu, PrevoziSe) values (@idP, @courier, 0, 0)
			
			-- save backup
			if(@idP not in (select ID from PAKET_TEMP))
			begin
				select @status = Status, @currentAddress = TrenutnaLokacija from PAKET where Id = @idP
				select @toDeliver = ZaDostavu from PLAN_VOZNJE where IdPaket = @idP
					
				insert into PAKET_TEMP (Id, TrenutnaLokacija, ZaDostavu, Status, Kurir)
				values (@idP, @currentAddress, @toDeliver, @status, @courier)

				select @tmpStr = coalesce(@currentAddress, -1)
				print('adresa: ' + cast(@tmpStr as varchar))
			end
			
			print('dodat-pickup2-home-' + cast(@idP as varchar))
			set @success = 1
		end
			
		fetch next from @cursorHome
		into @idP, @weight
	end
	
	close @cursorHome
	deallocate @cursorHome
	
	-- ------------------------------------------------------------------------------------------- --
	
	-- get all possible packages from storage
	set @cursorStorage = cursor for
	select Id, Tezina
	from PAKET
	where (
		Status = 2 
		and 
		(select IdGrad from ADRESA where Id = PAKET.TrenutnaLokacija) = @city
		and 
		PAKET.Id not in (select IdPaket from PLAN_VOZNJE)
	)
	order by VremePrihvatanja ASC

	open @cursorStorage

	fetch next from @cursorStorage
	into @idP, @weight

	-- plan what packages to take until it reaches car maximum capacity (greedy order by time)
	while @@FETCH_STATUS = 0
	begin
		if(@loaded + @weight <= @capacity)
		begin
			set @loaded = @loaded + @weight

			insert into PLAN_VOZNJE (IdPaket, Kurir, ZaDostavu, PrevoziSe) values (@idP, @courier, 0, 0)
			
			-- save backup
			if(@idP not in (select ID from PAKET_TEMP))
			begin
				select @status = Status, @currentAddress = TrenutnaLokacija from PAKET where Id = @idP
				select @toDeliver = ZaDostavu from PLAN_VOZNJE where IdPaket = @idP
					
				insert into PAKET_TEMP (Id, TrenutnaLokacija, ZaDostavu, Status, Kurir)
				values (@idP, @currentAddress, @toDeliver, @status, @courier)

				select @tmpStr = coalesce(@currentAddress, -1)
				print('adresa: ' + cast(@tmpStr as varchar))
			end
			
			print('dodat-pickup2-storage-' + cast(@idP as varchar))
			set @success = 1
		end
			
		fetch next from @cursorStorage
		into @idP, @weight
	end
	
	close @cursorStorage
	deallocate @cursorStorage

	return @success
end
go