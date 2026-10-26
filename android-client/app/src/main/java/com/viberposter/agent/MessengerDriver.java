package com.viberposter.agent;

interface MessengerDriver {
    void openChat(String chatName) throws AgentException;
    void sendText(String text) throws AgentException;
    void editMessage(String oldText, String newText) throws AgentException; 
    void deleteEverywhere(String text) throws AgentException;
}   