use KurirskaSluzba
go

drop function if exists fGetCar
go

create function fGetCar (
	@courier varchar(100)
)
	returns varchar(100)
as
begin
	declare @reg varchar(100)
	declare @city int
	
	-- get city
	select @city = IdGrad from ADRESA where Id = (select IdAdresa from KORISNIK where KorisnickoIme = @courier)

	-- get car id
	select top(1) @reg = RegistracioniBroj
	from VOZILO
	where 
	(select IdGrad from ADRESA where Id = VOZILO.IdAdresa) = @city 
	and
	not exists (select * from KURIR where RegistracioniBroj = VOZILO.RegistracioniBroj)
	
	return @reg
end
go