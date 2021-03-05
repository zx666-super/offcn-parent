package com.offcn.user.service;

import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;

import java.util.List;

public interface UserService {
    void registerUser(TMember member);
    TMember login(String username,String password);
    TMember selectById(Integer id);
    List<TMemberAddress> findAddressList(Integer memberId);
}
