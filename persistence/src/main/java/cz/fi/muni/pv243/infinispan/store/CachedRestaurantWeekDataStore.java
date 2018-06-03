package cz.fi.muni.pv243.infinispan.store;

import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantWeekDataStore;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

@Named
@ApplicationScoped
@CachedStore
public class CachedRestaurantWeekDataStore implements RestaurantWeekDataStore {

    @Inject
    @JPAStore
    private RestaurantWeekDataStore delegate;

    @Inject
    @DefaultCacheConfiguration
    private Cache<Long, RestaurantWeekData> restaurantWeekDataCache;


    @Override
    @Transactional
    public RestaurantWeekData addWeekData(RestaurantWeekData data) {
        data = delegate.addWeekData(data);
        restaurantWeekDataCache.put(data.getId(), data);
        return data;
    }

    @Override
    @Transactional
    public RestaurantWeekData updateWeekData(RestaurantWeekData data) {
        data = delegate.updateWeekData(data);
        restaurantWeekDataCache.replace(data.getId(), data);
        return data;
    }

    @Override
    @Transactional
    public void deleteWeekData(RestaurantWeekData data) {
        delegate.deleteWeekData(data);
        restaurantWeekDataCache.remove(data.getId(), data);
    }

    @Override
    public List<RestaurantWeekData> getAllWeekData() {
        return delegate.getAllWeekData();
    }

    @Override
    public RestaurantWeekData getThisWeekData(String googlePlaceID, int weekNumber){
        QueryFactory queryFactory = Search.getQueryFactory(restaurantWeekDataCache);
        Query query = queryFactory
                .from(RestaurantWeekData.class)
                .having("restaurant.googlePlaceID")
                .eq(googlePlaceID)
                .and()
                .having("weekNumber")
                .eq(weekNumber)
                .toBuilder().build();
        List<RestaurantWeekData> result = query.list();
        if(result.isEmpty()){
            RestaurantWeekData inDB = delegate.getThisWeekData(googlePlaceID, weekNumber);
            if(inDB!=null){
                restaurantWeekDataCache.put(inDB.getId(),inDB);
            }
            return inDB;
        }else{
            return result.get(0);
        }
    }
}