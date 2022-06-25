use KurirskaSluzba
go

drop trigger if exists trAddCourier
go

create trigger trAddCourier
	on KURIR
	after insert
as
begin
	declare @username varchar(100)
	declare @cursorI cursor

	set @cursorI = cursor for
	select KorisnickoIme
	from inserted

	open @cursorI

	fetch next from @cursorI
	into @username

	while @@FETCH_STATUS = 0
	begin
		delete from ZAHTEV where KorisnickoIme = @username
		
		update KURIR 
		set TrenutnaLokacija = (select IdAdresa from KORISNIK where KorisnickoIme = @username)
		where KorisnickoIme = @username

		fetch next from @cursorI
		into @username
	end

	close @cursorI
	deallocate @cursorI
	
end
go