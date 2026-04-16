#!/bin/bash
toggle_file='./backup_toggle'
if [ -f $toggle_file ];
then
    echo "Toggle file exists!"
    echo "Toggle value " `cat $toggle_file`
else
    echo "Toggle file doesn't exist, creating..."
    echo 0 > $toggle_file
fi;
curr_date=`date '+%d-%b-%Y'`
curr=`cat $toggle_file`
next=`expr 1 - $curr`
echo $next > $toggle_file
backup_file_dmp=dbsql_$curr.dmp
backup_file_tar=data_$curr.tar

su - postgres -c "pg_dump -U nrsc erecruit > $backup_file_dmp"

mv /var/lib/pgsql/$backup_file_dmp /root

scp /root/$backup_file_dmp root@172.19.2.20:/data/erecruit/

tar -C /data  -cvf /imgarchive/Erecruit/$backup_file_tar Recruitment_data ehiring.properties_applicant_live ehiring.properties_admin_live Logs_admin Logs_live

scp /imgarchive/Erecruit/$backup_file_tar root@172.19.2.20:/data/erecruit/

rm /imgarchive/Erecruit/$backup_file_tar

rm /root/$backup_file_dmp
