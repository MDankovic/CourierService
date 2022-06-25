use KurirskaSluzba
go

drop trigger if exists trCourierRequestCheck
go

create trigger trCourierRequestCheck
	on ZAHTEV
	after insert
as
begin
	declare @username varchar(100)
	declare @driversLicenceNumber varchar(100)
	declare @cursorI cursor

	set @cursorI = cursor for
	select KorisnickoIme, BrojVozackeDozvole
	from inserted

	open @cursorI

	fetch next from @cursorI
	into @username, @driversLicenceNumber

	while @@FETCH_STATUS = 0
	begin
		if((select COUNT(*) from KURIR where KorisnickoIme = @username) > 0)
		begin
			raiserror('User is already courier', 11, 1)
			rollback transaction;
			break;
		end

		fetch next from @cursorI
		into @username, @driversLicenceNumber
	end

	close @cursorI
	deallocate @cursorI
	
end
go