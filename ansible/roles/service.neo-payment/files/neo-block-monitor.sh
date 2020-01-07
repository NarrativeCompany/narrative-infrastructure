#!/bin/bash
/bin/journalctl --unit=neo-payment -n 1000 --no-pager | awk '/ Block /{save=$(NF-2)}END{print "neo_block{block_type=\"current\"} "save}' > /opt/textfile_collector/neo-block-monitor.prom
/bin/journalctl --unit=neo-payment -n 1000 --no-pager | awk '/ Block /{save=$(NF)}END{print "neo_block{block_type=\"best\"} "save}' >> /opt/textfile_collector/neo-block-monitor.prom
