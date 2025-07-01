package com.example.grecords;

import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

    private static MemberRepository instance;
    private final ArrayList<MemberModelClass> memberList;

    private MemberRepository() {
        memberList = new ArrayList<>();
    }

    public static synchronized MemberRepository getInstance() {
        if (instance == null) {
            instance = new MemberRepository();
        }
        return instance;
    }

    public List<MemberModelClass> getMembers() {
        return new ArrayList<>(memberList);
    }

    public void addMember(MemberModelClass member) {
        memberList.add(member);
    }

    public void removeMember(MemberModelClass member) {
        memberList.remove(member);
    }

    public void updateMember(int index, MemberModelClass member) {
        memberList.set(index, member);
    }

    public void clear() {
        memberList.clear();
    }
}
