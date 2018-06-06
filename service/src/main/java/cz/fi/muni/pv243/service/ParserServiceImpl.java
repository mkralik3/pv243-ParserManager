/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service;

import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserStore;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Michaela Bocanova
 */
@Named
@ApplicationScoped
public class ParserServiceImpl implements ParserService {

    @Inject
    @CachedStore
    private CachedParserStore parserStore;

    @Inject
    @RequestScoped
    private QueueSenderSessionBean senderSessionBean;

    @Override
    public void confirm(long parserId) {
        Parser parser = parserStore.findParser(parserId);

        Parser old = getConfirmedParser(parser.getRestaurant(), parser.getDay());
        if (old != null) {
            old.setConfirmed(false);
            parserStore.updateParser(old);
        }

        parser.setConfirmed(true);
        parserStore.updateParser(parser);

        senderSessionBean.sendMessage("confirm: " + String.valueOf(parser.getId()));
    }

    @Override
    public Parser addParser(Parser parser) {
        Parser p = parserStore.addParser(parser);
        senderSessionBean.sendMessage("create: " + String.valueOf(parser.getId()));
        return p;
    }

    @Override
    public List<Parser> getAllParsers(boolean confirmed) {
        return parserStore.getAllParsers(confirmed);
    }

    @Override
    public Parser getConfirmedParser(Restaurant restaurant, Day day) {
        if(restaurant==null){
            throw new IllegalArgumentException("Restaurant is null");
        }
        return  parserStore.getConfirmedParser(restaurant.getGooglePlaceID(), day);
    }

    @Override
    public Parser updateParser(Parser p) {
        Parser parser = parserStore.findParser(p.getId());

        parser.setDay(p.getDay());
        parser.setConfirmed(p.isConfirmed());
        parser.setXpath(p.getXpath());

        parserStore.updateParser(parser);

        senderSessionBean.sendMessage("updated: " + String.valueOf(parser.getId()));

        return parser;
    }

}
