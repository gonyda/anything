package com.bbsk.anything.user.repository;

import com.bbsk.anything.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    /* TODO dto로 받기 */
    User findUserAndNewsKeyword(String userId);
}
