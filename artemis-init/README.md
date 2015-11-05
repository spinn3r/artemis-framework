# Overview

The artemis-init framework provides an init service system that allow us to load
services from config files on disk but also communicate dependencies to other 
services in the system.

## Services

If you have a new system you want to integrate with your daemon, usually something
complicated which requires a configuration file, and complex startup, background threads, etc, then you would just implement it as a service.

## Advertisements

The core of the system is designed around advertisements.

When your service starts you would just advertise your services.

For example,

```
advertise( CreditCardProvider.class, myCreditCardProvider );
```

external code than then call

```
CreditCardProvider creditCardProvider = advertisements.require( CreditCardProvider.class );
``` 

## Dependency graph

Services provide annotations with the ```Launcher``` uses to determine if we have 
met the requirements to launch a service.  If a services Requires a Foo.class 
but no service Advertises it then the launcher will fail on startup during 
```launch```.

## Tracing

Tracing is the ability to write to a log channel while services start.

One issue is where do these services get sent?  Standard output? Log4j?  

If you haven't initialized log4j yet then you can't write messages to it.

We provide a simple tracing infra that allows info and error level logging 
similar to log4j but allows you to advertise the tracer.  

## Configuration files

Config files can be loaded from JSON and defined in a ServiceDescriptor.  They 
can be either required or optional. The format is either JSON or HOCON and they 
are reflected into regular Java POJOs.

External configuration files, or side files, can be loaded as well by using the 
ConfigLoader with the service and reading the file manually by path name.

The way files are loaded is injected in the constructor of the launcher.  We 
can load from resources, or a regular filesystem directory.

Paths are relative to the ConfigLoader which allows us to load from a specific 
filesystem directory like /etc or from resources in /config.

### Config Advertisements

All config objects are instantiated, then parsed, then placed in an advertisement.

This way anyone can read them including the service it's associated with.

## TODO:

- on stop() we have to forget() services .. we also need to make sure we can't 
  forget a message advertised by another service. 
  
- print the dependency graph of services at runtime so that I can debug what's 
  happening
 
- keep track of which service advertised which class

- keep track of service launch order and print it..

- support loading multiple config files using many @Config annotations.

- we can't replace service or bindings. This makes testing much much harder.
  we should support some type of way to replace services that are only 
  started on demand. This way if we replace a binding, the service isn't started
  because all the bindings are replaced.
  
  - What we could do is see that Foo is advertised by FooService... 
   
  - but if have a BarService that replaces Foo then we shouldn't init FooService
  
  - 