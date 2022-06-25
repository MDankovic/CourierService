use KurirskaSluzba
go

drop procedure if exists spReturnToStockroom
go

create procedure spReturnToStockroom
	@courier varchar(100)
as
begin
	declare @stockroomAddress int
	declare @idP int
	declare @reg varchar(100)
	declare @fuelType int
	declare @fuelPricePerL decimal(10,3)
	declare @fuelConsumption decimal(10,3)
	declare @distance decimal(10,3)
	
	select @reg = RegistracioniBroj from KURIR where KorisnickoIme = @courier
	select @fuelType = TipGoriva, @stockroomAddress = IdAdresa, @fuelConsumption = Potrosnja from VOZILO where @reg = RegistracioniBroj

	-- set package address
	update PAKET
	set TrenutnaLokacija = @stockroomAddress
	where Id in (select IdPaket from PLAN_VOZNJE where Kurir = @courier)

	-- delete returned packages from plan
	delete from PLAN_VOZNJE
	where Kurir = @courier

	-- update vehicle info
	update VOZILO
	set Zauzeto = 0
	where RegistracioniBroj = @reg

	set @fuelPricePerL = 
		case 
			when @fuelType = 0 then 15
			when @fuelType = 1 then 32
			when @fuelType = 2 then 36
		end

	--get covered distance from last stop to stockroom
	set @distance = dbo.fGetDistance((select TrenutnaLokacija from KURIR where KorisnickoIme = @courier), @stockroomAddress)

	-- update courier info
	update KURIR
	set RegistracioniBroj = null, Status = 0, Profit = Profit - @fuelPricePerL * (Distanca + @distance) * @fuelConsumption, TrenutnaLokacija = (select IdAdresa from KORISNIK where KorisnickoIme = @courier)
	where KorisnickoIme = @courier
	
	update KURIR
	set Distanca = 0
	where KorisnickoIme = @courier

end
go