package com.welab.k8s_backend_user.domain.repository;

import com.welab.k8s_backend_user.domain.SiteUser;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByUserId(String userId);
}
