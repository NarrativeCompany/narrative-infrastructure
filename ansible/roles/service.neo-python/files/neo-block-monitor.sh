#!/bin/bash
/bin/journalctl --unit=neo-python -n 1000 --no-pager | grep MainNet | tail -n 1 | awk '{ print "neo_block{block_type=\"current\"} " $12 }' > /opt/textfile_collector/neo-block-monitor.prom
/bin/journalctl --unit=neo-python -n 1000 --no-pager | grep MainNet | tail -n 1 | awk '{ print "neo_block{block_type=\"best\"} " $NF }' >> /opt/textfile_collector/neo-block-monitor.prom
