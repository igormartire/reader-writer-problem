make: clean build run

run: start stop

start: registry.PID server.PID

start-wp: registry.PID server.PID p=w

reboot: stop clean build start

reboot-wp: stop clean build start-wp

registry.PID:
	@echo STARTING RMI REGISTRY
	@cd build && { rmiregistry & echo $$! > ../$@; }
	@sleep 2

server.PID: registry.PID
	@echo STARTING PRIORITY SERVER
	@java -classpath build -Djava.rmi.server.codebase=file:build/ -Djava.rmi.server.hostname=localhost -DserverPort=33333 server.Server $(p) & echo $$! > $@
	@sleep 2

client:
	@java -classpath build -DremoteHost=localhost client.Client $(fk) $(ct) $(l)
	@sleep 2

stop:
	@echo KILLING RMI REGISTRY
	@kill `cat registry.PID 2> /dev/null` 2> /dev/null || true
	@rm registry.PID 2> /dev/null || true
	
	@echo KILLING RMI SERVER
	@kill `cat server.PID 2> /dev/null` 2> /dev/null || true
	@rm server.PID 2> /dev/null || true

build:
	@echo BUILDING PROJECT
	@mkdir build 2> /dev/null || true
	@javac -d build `find ./src/* | grep .java`

clean:
	@echo CLEANING PROJECT
	@rm -R build 2> /dev/null || true
	@rm *.txt 2> /dev/null || true

test:
	@make client fk=1 ct=pepe1 l=11 &
	@sleep 3
	@echo
	@make client fk=1 l=18 &
	@sleep 6
	@echo
	@make client fk=1 ct=pepe2 l=6 &
	@sleep 6
	@echo
	@make client fk=1 l=18 &

test-cc:
	@make client fk=1 ct=pepe1 l=11 &
	@sleep 3
	@echo
	@make client fk=2 ct=red l=12 &
	@sleep 2
	@echo
	@make client fk=1 l=18 &
	@sleep 2
	@echo
	@make client fk=2 l=5 &
	@sleep 6
	@echo
	@make client fk=2 ct=hot l=5 &
	@sleep 2
	@echo
	@make client fk=1 ct=pepe2 l=6 &
	@sleep 6
	@echo
	@make client fk=1 l=10 &
	@sleep 6
	@echo
	@make client fk=2 l=10 &

cloud-test:
	@make client fk=0 ct=pepe l=10000000 &
	@sleep 1
	@make client fk=0 l=10000000 &
.PHONY: run start stop build clean client
