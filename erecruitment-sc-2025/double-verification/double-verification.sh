#! /bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color
LOID_LIST='/home/nrsc/double-verification/loba_order_id_list.csv'
ALREADY_VERIFIED_LOID_LIST='/home/nrsc/double-verification/verified-list.csv'
DV_LOG_FILE='/home/nrsc/double-verification/double-verification.log'

dt=`date "+%Y-%m-%d %H:%M:%S"`
echo "$dt $0: CronJob STARTED" >> "$DV_LOG_FILE"
psql -U nrsc -d erecruit -c "SELECT loba_order_id 
                            FROM transaction_details 
                            WHERE ntrp_order_status IN ('SUCCESS','FAIL','BOOKED','PAYMENT INITIATED','Payment initiated','FORWARDED') 
                                    AND loba_id = 'NERECRUIT' 
                                    AND loba_order_id LIKE '20%';" > "$LOID_LIST.tmp"

tail -n +3 "$LOID_LIST.tmp" | tr -d ' ' | grep '^20'> "$LOID_LIST"
rm -f "$LOID_LIST.tmp"

COUNT=1
while read -r loba_order_id; 
do
    loba_order_id=`echo $loba_order_id | tr -d '\n'`
    echo -e "${COUNT} ${CYAN}$loba_order_id${NC}"

    if cat $ALREADY_VERIFIED_LOID_LIST | grep -w "$loba_order_id" >> /dev/null 
    then
        echo -n "Transaction has been verified recently, skiiped."
    else
        echo -n "Transaction is being verified."
        if java -classpath /home/nrsc/WEB-INF/lib/postgresql-42.2.5.jar:/home/nrsc/WEB-INF/lib/postgresql-42.2.5.jar/postgresql-9.1-903.jdbc4.jar:/home/nrsc/WEB-INF/classes/:/home/nrsc/WEB-INF/ehiring/action ehiring.action.Double_verification $loba_order_id 
        #>/dev/null
        then
            echo -e "${GREEN}[Done]${NC}"
            #echo "$loba_order_id" >> $ALREADY_VERIFIED_LOID_LIST
        else
            echo -e "${RED}[Failed]${NC}"
        fi;
    fi;
    echo ""
    COUNT=`expr $COUNT + 1`
    sleep 2
done < $LOID_LIST

dt=`date "+%Y-%m-%d %H:%M:%S"`
COUNT=`expr $COUNT - 1`
echo "$dt $0: Verified $COUNT transactions" >> "$DV_LOG_FILE"

dt=`date "+%Y-%m-%d %H:%M:%S"`
echo "$dt $0: CronJob COMPLETED" >> "$DV_LOG_FILE"
