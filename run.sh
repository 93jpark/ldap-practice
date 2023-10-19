cp ./init/env.startup.yaml ./environment/config/env.startup.yaml

docker-compose up

# docker run \
# 	--name ldap \
# 	-v ./environment/config:/container/environment/01-custom \
# 	-v ./init:/container/service/slapd/assets/config/bootstrap/ldif/custom \
# 	-p 389:389 \
# 	-p 636:636 \
# 	osixia/openldap:1.5.0 --loglevel debug --copy-service

