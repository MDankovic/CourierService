use KurirskaSluzba
go

drop function if exists fSameCity
go

create function fSameCity (
	@address1 int,
	@address2 int
)
	returns int
as
begin
 
	if(
		(select IdGrad from ADRESA where Id = @address1) = (select IdGrad from ADRESA where Id = @address2) 
	)
	begin
		return 1;
	end

	return 0
end
go