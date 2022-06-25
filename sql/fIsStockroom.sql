use KurirskaSluzba
go

drop function if exists fIsStockroom
go

create function fIsStockroom (
	@address int
)
	returns int
as
begin
	declare @res int = 0

	if((select count(*) from MAGACIN where IdAdresa = @address) > 0)
	begin 
		return 1
	end

	return 0
end
go