use KurirskaSluzba
go

drop procedure if exists spPlanRoute
go

create procedure spPlanRoute
	@courier varchar(100)
as
begin
	declare @reg varchar(100)
	declare @city int
	declare @capacity decimal(10,3)
	
	select @city = IdGrad from ADRESA where Id = (select IdAdresa from KORISNIK where KorisnickoIme = @courier)

	select @reg = RegistracioniBroj, @capacity = Nosivost
	from VOZILO
	where 
	(select IdGrad from ADRESA where Id = VOZILO.IdAdresa) = @city 
	and
	not exists (select * from KURIR where RegistracioniBroj = VOZILO.RegistracioniBroj)

	if(@reg is null)
	begin
		return -1;
	end

	if(not exists(
		select * from Paket
		where (
			(
				(Status = 1 and (select IdGrad from ADRESA where Id = AdresaOd) = @city) or 
				(Status = 2 and (select IdGrad from ADRESA where Id = TrenutnaLokacija) = @city and Kurir is null))
			)
			and
			Tezina <= @capacity
		)
	)

	begin
		return -1;
	end

	update KURIR set RegistracioniBroj = @reg, TrenutnaLokacija = (select IdAdresa from VOZILO where RegistracioniBroj = @reg) where KorisnickoIme = @courier

	exec [dbo].[spLoadPackages]
		@reg = @reg,
		@courier = @courier,
		@city = @city,
		@toDeliver = 1

	return 1;
end
go