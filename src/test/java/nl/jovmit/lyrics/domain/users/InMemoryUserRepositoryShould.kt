package nl.jovmit.lyrics.domain.users

class InMemoryUserRepositoryShould : UserRepositoryContract() {

    override fun repositoryWith(vararg users: User): UserRepository {
        return InMemoryUserRepository().apply {
            users.forEach { add(it) }
        }
    }
}