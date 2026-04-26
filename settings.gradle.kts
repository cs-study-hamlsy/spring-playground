rootProject.name = "spring-playground"

include("core")

include("jpa:dirty-checking")
include("jpa:n-plus-one")
include("jpa:fetch-join")
include("jpa:optimistic-lock")
include("jpa:batch-insert")

include("transaction:propagation")
include("transaction:isolation")
include("transaction:rollback")

include("concurrency:pessimistic-lock")
include("concurrency:distributed-lock")

include("spring-core:bean-lifecycle")
include("spring-core:aop")
