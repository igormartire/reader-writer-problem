# Readers-writers problem

## How to run

```
make build
make start p=r                  # starts RMI registry and server using ReadersPreferencePolicy
                                # other options are p=w for WritersPreferencePolicy
                                # and p=n for NoPreferencePolicy

make client fk=1 ct=asdf l=1    # writes "asdf" in resource1.txt, taking 1 second
make client fk=1 l=2            # reads from resource1.txt, taking 2 seconds
make client fk=2 ct=fdsa l=3    # writes "fdsa" in resource2.txt taking 3 seconds

make test                       # automatically create some clients

make stop                       # kill RMI registry and server
make clean                      # removes build folder and *.txt files
```

## References:
- https://en.wikipedia.org/wiki/Readers%E2%80%93writers_problem
- https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html