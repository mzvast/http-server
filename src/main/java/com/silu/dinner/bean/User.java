package com.silu.dinner.bean;

/**
 * Created by silu on 15-1-15.
 */
public class User {
    private String userId;
    private String account;
    private String mobile;
    private String nickName;
    private String password;
    private String gender;
    private String portraitFile;
    private String address;
    private String signature;
    private String birthdate;
    private String aliPayAccount;
    private String moneyAccount;
    private String tokenId;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPortraitFile() {
        return portraitFile;
    }

    public void setPortraitFile(String portraitFile) {
        this.portraitFile = portraitFile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAliPayAccount() {
        return aliPayAccount;
    }

    public void setAliPayAccount(String aliPayAccount) {
        this.aliPayAccount = aliPayAccount;
    }

    public String getMoneyAccount() {
        return moneyAccount;
    }

    public void setMoneyAccount(String moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", portraitFile='").append(portraitFile).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", signature='").append(signature).append('\'');
        sb.append(", birthdate='").append(birthdate).append('\'');
        sb.append(", aliPayAccount='").append(aliPayAccount).append('\'');
        sb.append(", moneyAccount='").append(moneyAccount).append('\'');
        sb.append(", tokenId='").append(tokenId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
