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
        target = self.find(user.username)
        target.name, target.email = user.name, user.email
    
    def list_all(self):
        return self.users
    
    # 2:53:51