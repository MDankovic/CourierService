use KurirskaSluzba
go

drop procedure if exists spDeleteAll
go

create procedure spDeleteAll
as
begin
	exec sp_MSForEachTable 'DISABLE TRIGGER ALL ON ?'

	exec sp_MSForEachTable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'

	exec sp_MSForEachTable 'SET QUOTED_IDENTIFIER ON; DELETE FROM ?'

	exec sp_MSForEachTable 'ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL'

	exec sp_MSForEachTable 'ENABLE TRIGGER ALL ON ?'
end
go