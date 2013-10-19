use warnings;
no warnings qw/uninitialized/;
use DBI;
use Data::Dumper;


$dbh = DBI->connect("DBI:mysql:database=genie;host=localhost:port=3306", "root" , "abc#123");

$sth = $dbh->prepare("SELECT * FROM user");

$sth->execute();

$numRows = $sth->rows;

print "\n rows : $numRows" . $sth->{'NUM_OF_FIELDS'};

use Data::Dumper;

my $row = $sth->fetchrow_hashref();

print Dumper($row);


my @trainingsessions =
([ '664a085d-bc9e-40ef-be1d-0b570ea03c35', 1 , '2013-07-03 18:23:10', '2013-07-03 18:23:10', 1,2,3,4,5,6,1,2,3,4,5,6, 1 , 1,1,1,1, 10.0, 20.0],
[ '664a085d-bc9e-40ef-be1d-0b570ea03c35', 2 , '2013-07-04 18:23:10', '2013-07-04 18:23:10', 1,2,3,4,5,6,1,2,3,4,5,6, 1,1,1,1,1 ,10.0, 20.0],
[ '664a085d-bc9e-40ef-be1d-0b570ea03c35', 8 , '2013-08-07 18:23:10', '2013-08-07 18:23:10', 1,2,3,4,5,6,1,2,3,4,5,6, 1 ,1,1,1,1, 10.0, 20.0],
[ '664a085d-bc9e-40ef-be1d-0b570ea03c35', 9 , '2013-08-08 18:23:10', '2013-08-08 18:23:10', 1,2,3,4,5,6,1,2,3,4,5,6, 1 ,1,1,1,1, 10.0, 20.0]); 


my $dbh = DBI->connect("DBI:mysql:database=genie;host=localhost:port=3306", "root", "abc#123");

# prepare sql
my $sql = qq{
insert 
into 
	fitness_training_session 
values (
	?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?
)
};

my $sth = $dbh->prepare($sql);

# foreach row, insert

foreach my $session (@trainingsessions) {
	$sth->execute(@{$session});
}

#$dbh->commit();


