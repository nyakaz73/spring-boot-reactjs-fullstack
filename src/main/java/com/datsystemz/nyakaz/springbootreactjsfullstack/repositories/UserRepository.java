package com.datsystemz.nyakaz.springbootreactjsfullstack.repositories;

import com.datsystemz.nyakaz.springbootreactjsfullstack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
