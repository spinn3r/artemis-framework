# artemis-corpus-test-framework

A test framework for working with test corpora for unit tests.

Say you have a complex string or HTML report that you want to test.

If you make a change to your code, this could impact the output of hundreds of
unit tests.

It would be nice to just verify the new output of all the tests, then, in one
pass, update all the tests by writing new resources.

## Enabling update mode globally:

You can specify:

```
-Dcorpora-asserter.update_mode=true
```

to your test framework which will force update mode globally.
