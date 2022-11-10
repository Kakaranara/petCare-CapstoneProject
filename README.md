# petCare-CapstoneProject

Dont forget to check package architecture.

MAIN PACKAGES : 
Here's the order for maintainable package : 
1. data -> network, database, repository
2. di (stands for dependency injection) -> modules that provide repository
3. UI  -> Fragment/Activity, ViewModel, Adapters (Ui should be separated per component by package, e.g. -> LoginPackage(fragment, view model, ..) , HomePackage(fragment, view model, adapter, ...), ) 
4. Utils -> if we have any utility function
