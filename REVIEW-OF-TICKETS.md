In general, I found the initial code to be quite well written, in the sense that it was understandable and not messy, but the design obviously needed restructuring. 
I also liked the usage of Estonian personal code validator, because this library helps ensure that the data used is valid.
The code itself was a basic version of a decision engine and error handling was quite minimal. 
Although the code itself was quite readable, it still wasn't SOLID enough to ensure future maintainability, 
because classes and methods were quite clumped together in DecisionEngine, whihc makes future improvements and testing more complicated.
The biggest shortcoming in my opinion was putting the business logic into the controller class. 