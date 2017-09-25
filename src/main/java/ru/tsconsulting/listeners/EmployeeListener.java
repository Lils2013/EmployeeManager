package ru.tsconsulting.listeners;

import org.hibernate.envers.RevisionListener;
public class EmployeeListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
    }
}
