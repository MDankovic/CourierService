use [KurirskaSluzba]
go

drop trigger if exists trTransportOffer
go

create trigger [dbo].[trTransportOffer]
	on [dbo].[PAKET]
	after insert
as
begin
	declare @idP int
	declare @type int
	declare @status int
	declare @startPrice decimal (10,3)
	declare @pricePerKG decimal (10,3)
	declare @weight decimal (10,3)
	declare @price decimal (10,3)
	declare @cursorI cursor
	declare @addressFrom int
	declare @addressTo int

	set @cursorI = cursor for
	select Id
	from inserted

	open @cursorI

	fetch next from @cursorI
	into @idP

	while @@FETCH_STATUS = 0
	begin
		select @type = Tip, @weight = Tezina, @status = Status, @addressFrom = AdresaOd, @addressTo = AdresaDo from PAKET where Id = @idP
		if(@status > 0)
		begin
			fetch next from @cursorI
			into @idP
			continue
		end

		if(@status != 0)
		begin
			rollback transaction;
			break;
		end

		set @startPrice = 
		case 
			when @type = 0 then 115
			when @type = 1 then 175
			when @type = 2 then 250
			when @type = 3 then 350
		end

		set @pricePerKG = 
		case 
			when @type = 0 then 0
			when @type = 1 then 100
			when @type = 2 then 100
			when @type = 3 then 500
		end

		set @price = (@startPrice + @pricePerKG * @weight) * dbo.fGetDistance(@addressFrom, @addressTo)
		
		update PAKET set Cena = @price, TrenutnaLokacija = AdresaOd where Id = @idP

		fetch next from @cursorI
		into @idP
	end

	close @cursorI
	deallocate @cursorI
	
end
