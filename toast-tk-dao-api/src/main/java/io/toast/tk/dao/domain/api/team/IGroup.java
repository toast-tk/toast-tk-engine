package io.toast.tk.dao.domain.api.team;

import java.util.List;

import io.toast.tk.dao.domain.impl.test.block.IProject;

public interface IGroup {

	List<IProject> getProjects();

	List<IUser> getUsers();

}
