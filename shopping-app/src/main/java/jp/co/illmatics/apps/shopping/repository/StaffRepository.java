package jp.co.illmatics.apps.shopping.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jp.co.illmatics.apps.shopping.entity.Staff;

public interface StaffRepository extends CrudRepository<Staff, Long> {

	/**
	 * Staffを全件取得
	 * @return StaffのList
	 */
	List<Staff> findAll();
}