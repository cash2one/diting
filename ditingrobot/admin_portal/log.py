import logging
import logging.config
CONF_LOG = "logging.conf"
logging.config.fileConfig(CONF_LOG)
logger = logging.getLogger()