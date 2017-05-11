#!/bin/bash
# to examine startup issue, add: --preload --log-file=-
# uncomment the following line to deploy on PROD
# export FLASK_ENV=PROD-LEGACY

if [[ -z "$FLASK_ENV" ]]; then
	echo "WARNING! FLASK_ENV not set, will use **DEV** by default."
fi

THREAD_COUNT=8
if [[ $FLASK_ENV == 'PROD-LEGACY' ]] || [[ $FLASK_ENV == 'PROD' ]]; then
	THREAD_COUNT=128
fi

echo "Environment: $FLASK_ENV"
echo "Thread Count: $THREAD_COUNT"
gunicorn -k tornado -w $THREAD_COUNT -b 0.0.0.0:4100 web_antusheng:app -t 6000000
