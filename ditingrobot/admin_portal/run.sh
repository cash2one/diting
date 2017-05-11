#!/bin/bash

gunicorn -k tornado -w 8 -b 0.0.0.0:5100 admin_portal:app -t 6000000

