class UserDatabase:
    def __init__(self):
        self.users = []
    
    def insert(self, user):
        i = 0
        while i < len(self.users):
            # find the first user username greater than the new user's username
            if self.users[i].username > user.username:
                break
            i += 1
        self.users.insert(i, user)
    
    def find(self, username):
        for user in self.users:
            if user.username == username:
                return username
    
    def update(self, user):
        target = self.find(users.username)
        target.name, target.email = user.name, user.email
    
    def list_all(self):
        return self.users
    
    def delete(self, user):
        target = self.find(users.username)
        if target:
            users.remove(target)
        else:
            pass
    

if __name__ == "__main__":
    user_1 = User("kakasungura", "kaka sungura", "kakasungura@bunnyracing.co")
    user_2 = User("kakambweha", "kaka mbweha", "kakambweha@forfoxsake.ac.ke")
    user_3 = User("goat matata", "goat matata", "goat matata@thegoatpodcast.com")
    user_4 = User(username="ddangombe", name="dada ngombe...", email="dadangombe@chizicheese.co.k")

    user_database = UserDatabase()
    user_database.insert(user_1)
    user_database.insert(user_2)
    user_database.insert(user_3)

    find_user_2 = user_database.find()
    user_database.list_all()
    user_database.delete(user_1)
    user_database.list_all()
    user_database.update(username="dadangombe", name="dada ngombe", email="dadangombe@chizicheese.co.ke")
    user_database.list_all()
    # 2:54:50