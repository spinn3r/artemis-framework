# Overview

- TODO:

- create a rethrow OBJECT that we create so that we can get the cause with 
  generics.  Otherwise we will have to do soem weird work trying to figure 
  out what to cast teh class as...
  
  
  Rethrower<MyException> rethrower = new Rethrower();
  
  rethrower.handle( -> {

    return foo.stream().map(rethrower.evaluate( -> {
    
    } ).collect();
  
  } );
  
- TODO: how do I create the NEW exception... ?? We don't have the typo info
  from the generics.