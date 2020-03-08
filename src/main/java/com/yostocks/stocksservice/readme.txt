#source: https://axiomq.com/blog/backup-and-restore-a-postgresql-database/
#to dump database from command line:
pg_dump -U postgres -W -F p stocks > c:\users\quena\stocks.sql

#restore database:
psql -U db_user db_name < dump_name.sql

