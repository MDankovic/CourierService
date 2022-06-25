use KurirskaSluzba
go

drop procedure if exists spGetNextStop
go

create procedure spGetNextStop
	@courier varchar(100),
	@newCity int output
as
begin
	-- courier info
	declare @currentAddress int
	declare @currentCity int

	-- package info
	declare @myCursor cursor
	declare @packageAddress int
	declare @packageCity int
	declare @idP int
	declare @weight decimal(10,3)
	declare @capacity decimal(10,3)
	declare @loaded decimal(10,3)

	-- new city flag set to false
	set @newCity = 0

	-- get current courier location (city, address)
	select @currentAddress = TrenutnaLokacija from KURIR where KorisnickoIme = @courier
	select @currentCity = IdGrad from ADRESA where Id = @currentAddress

	-- get package to deliver in the current city
	select top(1) @idP = IdPaket
	from PLAN_VOZNJE
	where 
	Kurir = @courier 
	and 
	ZaDostavu = 1 
	and 
	PrevoziSe = 1
	and
	dbo.fSameCity(@currentAddress, (select AdresaDo from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket)) = 1
	and
	(select Status from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket) = 2
	order by dbo.fGetDistance(@currentAddress, (select AdresaDo from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket))

	-- if there is no package to deliver, get next package for pick up from ADDRESS int the current city
	if(@idP is null)
	begin
		select top(1) @idP = IdPaket
		from PLAN_VOZNJE
		where 
		Kurir = @courier
		and 
		dbo.fSameCity(@currentAddress, (select AdresaOd from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket)) = 1
		and 
		(select Status from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket) = 1
		order by (select VremePrihvatanja from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket) ASC
		--dbo.fGetDistance(@currentAddress, (select TrenutnaLokacija from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket))
	end
	
	-- if there is no package to deliver, get next package for pick up from STORAGE int the current city
	if(@idP is null)
	begin
		select top(1) @idP = IdPaket
		from PLAN_VOZNJE
		where 
		Kurir = @courier
		and 
		dbo.fSameCity(@currentAddress, (select TrenutnaLokacija from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket)) = 1
		and 
		(select Status from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket) = 2
		order by (select VremePrihvatanja from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket) ASC
	end

	-- get next package to deliver
	if(@idP is null)
	begin
		select top(1) @idP = IdPaket
		from PLAN_VOZNJE
		where 
		Kurir = @courier 
		and 
		ZaDostavu = 1
		order by dbo.fGetDistance(@currentAddress, (select AdresaDo from PAKET where PAKET.Id = PLAN_VOZNJE.IdPaket))

		-- new city flag set to true
		set @newCity = 1
	end

	-- if there are no more packages to deliver or pick up, return to the stockroom (-1)
	if(@idP is null)
	begin
		return -1
	end
	else
	begin
		return @idP
	end
end
go