use KurirskaSluzba
go

drop procedure if exists spDeliverPackages
go

create procedure spDeliverPackages
	@courier varchar(100),
	@city int
as
begin
	declare @myCursor cursor
	declare @idP int
	declare @reg varchar(100)
	declare @weight decimal(10,3)
	declare @capacity decimal(10,3)
	declare @price decimal(10,3)
	declare @loaded decimal(10,3)

	select @reg = RegistracioniBroj from KURIR where KorisnickoIme = @courier
	select @capacity = Nosivost, @loaded = Zauzeto from VOZILO where RegistracioniBroj = @reg

	set @myCursor = cursor for
	select Id, Tezina, Cena
	from Paket
	where Kurir = @courier and AdresaDo = @city and TrenutnaLokacija is null

	open @myCursor

	fetch next from @myCursor
	into @idP, @weight, @price

	while @@FETCH_STATUS = 0
	begin
		set @loaded = @loaded - @weight

		update PAKET
		set Status = 3, TrenutnaLokacija = null, Kurir = null
		where Id = @idP

		update KURIR
		set Profit = Profit + @price
		where KorisnickoIme = @courier
			
		fetch next from @myCursor
		into @idP, @weight, @price
	end

	close @myCursor
	deallocate @myCursor

	update VOZILO
	set Zauzeto = @loaded
	where RegistracioniBroj = @reg

end
go