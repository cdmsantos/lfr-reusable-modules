package com.liferay.birthday.user.rest.application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

import com.liferay.birthday.service.UserFinderByBirthdayService;
import com.liferay.portal.kernel.json.JSONFactoryUtil;

/**
 * @author crystalsantos
 */
@Component(property = { JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/isl",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Greetings.Rest", "auth.verifier.guest.allowed=true",
		"liferay.access.control.disable=true" }, service = Application.class)
public class UserBirthdayServiceRestApplication extends Application {

	@Reference
	UserFinderByBirthdayService userBirthdayService;

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@GET
	@Path("/birthdays")
	@Produces("application/json")
	public String getBirthdays() {

		Locale chileLocale = new Locale("es", "CL");

		final DayOfWeek firstDayOfWeek = WeekFields.of(chileLocale).getFirstDayOfWeek();
		final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);

		LocalDate start = LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
		LocalDate end = LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek));

		return JSONFactoryUtil.serialize(userBirthdayService.findUserByBirthday(start, end));
	}

}