package com.company.cruisesample.service;

import com.company.cruisesample.entity.Port;
import com.company.cruisesample.entity.Waypoint;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(RoutingService.NAME)
public class RoutingServiceBean implements RoutingService {

    @Inject
    protected Metadata metadata;

    @Override
    public List<Waypoint> calculateRoute(List<Port> ports) {
        List<Waypoint> result = new ArrayList<>();
        for (Port p : ports) {
            Waypoint wp = metadata.create(Waypoint.class);
            wp.setPoint(p.getLocation());
            if (result.size() == 0)
                wp.setEta(new Date());
            else {
                Waypoint prev = result.get(result.size() - 1);
                Date newDate = DateUtils.addDays(prev.getEta(), 1);
                wp.setEta(newDate);
            }
            result.add(wp);
        }
        return result;
    }
}