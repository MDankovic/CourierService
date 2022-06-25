use KurirskaSluzba
go

drop procedure if exists spLoadPackages
go

create procedure spLoadPackages
	@reg varchar(100),
	@courier varchar(100),
	@city int,
	@toDeliver int
as
begin
	declare @myCursor cursor
	declare @idP int, @sender int, @recv int
	declare @weight decimal(10,3)
	declare @capacity decimal(10,3)
	declare @loaded decimal(10,3)
	declare @distance decimal(10,3)

	select @capacity = Nosivost, @loaded = Zauzeto from VOZILO where RegistracioniBroj = @reg

	set @myCursor = cursor for
	select Id, Tezina, AdresaOd, AdresaDo
	from PAKET
	where 
	(Status = 1 and (select IdGrad from ADRESA where Id = PAKET.AdresaOd) = @city) or 
	(Status = 2 and (select IdGrad from ADRESA where Id = PAKET.TrenutnaLokacija) = @city)
	order by Status, VremePrihvatanja

	open @myCursor

	fetch next from @myCursor
	into @idP, @weight, @sender, @recv

	while @@FETCH_STATUS = 0
	begin
		if(@loaded + @weight <= @capacity)
		begin
			set @loaded = @loaded + @weight

			if(@toDeliver = 0)
			begin
				update Paket
				set Status = 2, TrenutnaLokacija = (select IdAdresa from VOZILO where RegistracioniBroj = @reg), KorisnickoIme = @courier
				where Id = @idP
			end
			else
			begin
				update Paket
				set Status = 2, TrenutnaLokacija = null, KorisnickoIme = @courier
				where Id = @idP
			end
		end
		else 
			break;
			
		fetch next from @myCursor
		into @idP, @weight, @sender, @recv
	end

	close @myCursor
	deallocate @myCursor

	update VOZILO
	set Zauzeto = @loaded
	where RegistracioniBroj = @reg

end
go