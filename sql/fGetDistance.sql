use KurirskaSluzba
go

drop function if exists fGetDistance
go

create function fGetDistance (
	@address1 int,
	@address2 int
)
	returns decimal(10, 3)
as
begin
	declare @distance decimal(10,3)
 
	select @distance = cast(sqrt(power((A1.X - A2.X), 2) + power((A1.Y - A2.Y), 2)) as decimal(10, 3)) 
	from ADRESA A1, ADRESA A2 
	where A1.Id = @address1 and A2.Id = @address2

	return @distance
end
go