package com.Inkspire.ecommerce.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface IUserRepository extends JpaRepository<User, Long>{
    List<User> findByAtivoTrue();
}
