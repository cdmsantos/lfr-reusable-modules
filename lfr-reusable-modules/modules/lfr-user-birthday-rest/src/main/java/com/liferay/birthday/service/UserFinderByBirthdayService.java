package com.liferay.birthday.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import com.liferay.birthday.user.dto.UserDTO;
import com.liferay.birthday.user.dto.UsersDTO;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexSearcherHelperUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.search.generic.TermRangeQueryImpl;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * @author crystalsantos
 */
@Component(immediate = true, property = {}, service = UserFinderByBirthdayService.class)
public class UserFinderByBirthdayService {

	private static final String USER_CLASS = "com.liferay.portal.kernel.model.User";
	private static final String ENTRY_CLASS_NAME = "entryClassName";

	public UsersDTO findUserByBirthday(LocalDate start, LocalDate end) {

		try {
			SearchContext sc = new SearchContext();
			sc.setCompanyId(PortalUtil.getDefaultCompanyId());

			String[] classNames = { USER_CLASS };

			sc.setEntryClassNames(classNames);

			TermQuery userQuery = new TermQueryImpl(ENTRY_CLASS_NAME, USER_CLASS);

			BooleanQuery booleanQuery = new BooleanQueryImpl();
			booleanQuery.add(userQuery, BooleanClauseOccur.MUST);

			String startString = start.getYear() + getNumber(start.getMonthValue()) + getNumber(start.getDayOfMonth())
					+ "000000";

			String endString = end.getYear() + getNumber(end.getMonthValue()) + getNumber(end.getDayOfMonth())
					+ "000000";

			
			TermRangeQuery termRangeQuery = new TermRangeQueryImpl(
					   "birthday",startString, endString, true,
					   true);
			booleanQuery.add(termRangeQuery, BooleanClauseOccur.MUST);
			
			Hits hits = IndexSearcherHelperUtil.search(sc, booleanQuery);
			Document[] userDocuments = hits.getDocs();

			if (userDocuments != null) {
				_log.info(userDocuments.length
						+ " results were found for the query.");
				List<UserDTO> listOfUsers = new ArrayList<UserDTO>();
				for (Document document : userDocuments) {
					_log.debug(document);
					User user = UserLocalServiceUtil.fetchUser(Long.valueOf(document.get("userId")));
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(user.getBirthday());

					listOfUsers.add(new UserDTO(getNumber(calendar.get(Calendar.DAY_OF_MONTH)),
							getNumber(calendar.get(Calendar.MONTH)+1), user.getJobTitle(), user.getFullName()));

				}

				return new UsersDTO(listOfUsers);

			} else {
				_log.info("No results were found for the query:");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getNumber(int number) {
		if (number > 0 && number < 10) {
			return String.format("%02d", number);
		} else {
			return Integer.toString(number);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(UserFinderByBirthdayService.class);
}
