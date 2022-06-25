use KurirskaSluzba
go

drop trigger if exists trStockroomCheck
go

create trigger trStockroomCheck
	on MAGACIN
	instead of insert
as
begin
	declare @idC int
	declare @idA int
	declare @cursorI cursor
	declare @testNum int

	set @cursorI = cursor for
	select IdAdresa
	from inserted

	open @cursorI

	fetch next from @cursorI
	into @idA

	while @@FETCH_STATUS = 0
	begin
		select @idC = IdGrad from ADRESA where Id = @idA
		
		if((select COUNT(*) from ADRESA join MAGACIN on ADRESA.Id = MAGACIN.IdAdresa where ADRESA.IdGrad = @idC) > 0)
		begin
			raiserror('Stockroom already exists int that city', 11, 1)
			rollback transaction;
			break;
		end
		else
		begin
			insert into MAGACIN (IdAdresa, IdGrad) VALUES (@idA, @idC)
		end

		fetch next from @cursorI
		into @idA
	end

	close @cursorI
	deallocate @cursorI
	
end
go