use KurirskaSluzba
go

drop procedure if exists spPlanPickup
go

create procedure spPlanPickup
	@courier varchar(100),
	@reg varchar(100),
	@toDeliver int = 0
as
begin
	-- courier info
	declare @city int
	declare @courierAddress int
	declare @currentAddress int

	-- package info
	declare @myCursor cursor
	declare @idP int
	declare @weight decimal(10,3)
	declare @capacity decimal(10,3)
	declare @loaded decimal(10,3)

	-- operation success
	declare @success int = 0

	-- get city
	select @city = IdGrad, @courierAddress = Id from ADRESA where Id = (select IdAdresa from KORISNIK where KorisnickoIme = @courier)

	-- get courier location
	select @currentAddress = TrenutnaLokacija from KURIR where KorisnickoIme = @courier

	-- get car capacity and current load
	select @capacity = Nosivost, @loaded = Zauzeto from VOZILO where RegistracioniBroj = @reg

	-- get all possible packages
	set @myCursor = cursor for
	select Id, Tezina
	from PAKET
	where (
		(Status = 1 and (select IdGrad from ADRESA where Id = PAKET.AdresaOd) = @city) 
		and 
		PAKET.Id not in (select IdPaket from PLAN_VOZNJE)
	)
	order by VremePrihvatanja

	open @myCursor

	fetch next from @myCursor
	into @idP, @weight

	-- plan what packages to take until it reaches car maximum capacity (greedy order by time)
	while @@FETCH_STATUS = 0
	begin
		if(@loaded + @weight <= @capacity)
		begin
			set @loaded = @loaded + @weight

			insert into PLAN_VOZNJE (IdPaket, Kurir, ZaDostavu, PrevoziSe) values (@idP, @courier, @toDeliver, 0)
			
			set @success = 1
		end
		else 
			break;
			
		fetch next from @myCursor
		into @idP, @weight
	end
	
	close @myCursor
	
	-- get all possible packages
	set @myCursor = cursor for
	select Id, Tezina
	from PAKET
	where (
		(Status = 2 and (select IdGrad from ADRESA where Id = PAKET.TrenutnaLokacija) = @city)
		and 
		PAKET.Id not in (select IdPaket from PLAN_VOZNJE)
	)
	order by VremePrihvatanja

	open @myCursor

	fetch next from @myCursor
	into @idP, @weight

	-- plan what packages to take until it reaches car maximum capacity (greedy order by time)
	while @@FETCH_STATUS = 0
	begin
		if(@loaded + @weight <= @capacity)
		begin
			set @loaded = @loaded + @weight

			insert into PLAN_VOZNJE (IdPaket, Kurir, ZaDostavu, PrevoziSe) values (@idP, @courier, @toDeliver, 0)
			
			set @success = 1
		end
		else 
			break;
			
		fetch next from @myCursor
		into @idP, @weight
	end

	-- update courier info
	if(@success = 1)
	begin
		update KURIR set RegistracioniBroj = @reg, Status = 1 where KorisnickoIme = @courier
	end
	
	close @myCursor
	deallocate @myCursor

	return @success
end
go