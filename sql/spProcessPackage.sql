use KurirskaSluzba
go

drop procedure if exists spProcessPackage
go

create procedure spProcessPackage
	@courier varchar(100),
	@package int
as
begin
	-- courier info
	declare @currentAddress int
	declare @currentCity int
	declare @reg varchar(100)

	-- package info
	declare @myCursor cursor
	declare @packageAddress int
	declare @packageCity int
	declare @toDeliver int
	declare @weight decimal(10,3)
	declare @price decimal(10,3)
	declare @distance decimal(10,3)
	declare @toDelete int = 0
	declare @idP int

	-- result
	declare @res int = 0
	
	-- get current courier location and car info (city, address, reg)
	select @currentAddress = TrenutnaLokacija, @reg = RegistracioniBroj from KURIR where KorisnickoIme = @courier
	select @currentCity = IdGrad from ADRESA where Id = @currentAddress

	-- get package info
	select @weight = Tezina, @price = Cena
	from PAKET
	where Id = @package

	select @toDeliver = ZaDostavu
	from PLAN_VOZNJE
	where IdPaket = @package

	-- get current package address
	select @packageAddress = TrenutnaLokacija from PAKET where PAKET.Id = @package

	-- get distance from current courier location to the next package
	-- if current package address is null, next step is to deliver it
	if(@toDeliver = 1 and @packageAddress is null)
	begin
		select @packageAddress = AdresaDo from PAKET where PAKET.Id = @package
		update PAKET set Status = 3, TrenutnaLokacija = AdresaDo where Id = @package
		update VOZILO set Zauzeto = Zauzeto - @weight where RegistracioniBroj = @reg
		update KURIR set Profit = Profit + @price, BrojPaketa = BrojPaketa + 1 where KorisnickoIme = @courier
		
		set @res = @package
		-- remove package from plan
		delete from PLAN_VOZNJE where IdPaket = @package
	end
	else
	begin
		update PAKET set Status = 2, TrenutnaLokacija = NULL where Id = @package
		update PLAN_VOZNJE set PrevoziSe = 1 where IdPaket = @package
		update VOZILO set Zauzeto = Zauzeto + @weight where RegistracioniBroj = @reg
		
		if((select dbo.fIsStockroom(@packageAddress)) = 1)
		begin
		----------------------------------------------------------------------------------
			set @myCursor = cursor for
			select Id, Tezina
			from PAKET
			where (
				TrenutnaLokacija = @packageAddress
				and 
				PAKET.Id in (select IdPaket from PLAN_VOZNJE where Kurir = @courier)
				
			)
			order by VremePrihvatanja ASC

			open @myCursor

			fetch next from @myCursor
			into @idP, @weight

			-- plan what packages to take until it reaches car maximum capacity (greedy order by time)
			while @@FETCH_STATUS = 0
			begin
				print('USAO U STOCKROOOOOOOM')
				update PAKET set Status = 2, TrenutnaLokacija = NULL where Id = @idP
				update PLAN_VOZNJE set PrevoziSe = 1 where IdPaket = @idP
				update VOZILO set Zauzeto = Zauzeto + @weight where RegistracioniBroj = @reg

				print('dodat-process-storage-' + cast(@idP as varchar))
					
				fetch next from @myCursor
				into @idP, @weight
			end
			
			close @myCursor
			deallocate @myCursor
			----------------------------------------------------------------------------------
		end
		
		set @res = -2
	end
	
	set @distance = dbo.fGetDistance(@currentAddress, @packageAddress)

	-- update courier location and total distance covered
	update KURIR 
	set Distanca = Distanca + @distance, TrenutnaLokacija = @packageAddress
	where KorisnickoIme = @courier
	

	return @res
end
go