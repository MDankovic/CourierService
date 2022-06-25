use KurirskaSluzba
go

drop procedure if exists spNextAddress
go

create procedure spNextAddress
	@courier varchar(100)
as
begin
	declare @reg varchar(100)
	declare @city int
	declare @currAddress int
	declare @currX decimal(10, 3)
	declare @currY decimal(10, 3)
	declare @nextAddress int
	
	-- get current location
	select @currAddress = Id, @currX = X, @currY = Y from ADRESA where Id = (select TrenutnaLokacija from KURIR where KorisnickoIme = @courier)
	
	-- get car id
	select @reg = RegistracioniBroj from KURIR where KorisnickoIme = @courier

	-- get next address, city
	select top(1) @nextAddress = AdresaDo 
	from PAKET
	where 
	Kurir = @courier and 
	ZaDostavu = 1
	order by dbo.fGetDistance(@currAddress, PAKET.AdresaDo) asc

	return @nextAddress;
end
go