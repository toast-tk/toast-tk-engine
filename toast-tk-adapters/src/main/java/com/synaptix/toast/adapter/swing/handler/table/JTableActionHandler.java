package com.synaptix.toast.adapter.swing.handler.table;

import javax.swing.JTable;

import org.fest.swing.core.Robot;
import org.fest.swing.data.TableCell;
import org.fest.swing.data.TableCellByColumnId;
import org.fest.swing.fixture.JPopupMenuFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.TableCommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequestQueryCriteria;

public class JTableActionHandler implements
		ISwingwidgetActionHandler<JTable, String, TableCommandRequest> {

	@Override
	public String handle(JTable target, final TableCommandRequest command) {
		JTableFixture tFixture = new JTableFixture(
				FestRobotInstance.getRobot(), target);
		switch (command.action) {
		case COUNT:
			return handleCountAction(tFixture);
		case FIND:
			return handleFindAction(command, tFixture);
		case DOUBLE_CLICK:
			return handleDoubleClickAction(command, tFixture);
		case SELECT_MENU:
			return handleSelectMenu(command, tFixture);
		default:
			throw new IllegalArgumentException(
					"Unsupported command for JTable: " + command.action.name());
		}
	}

	private String handleSelectMenu(final TableCommandRequest command,
			JTableFixture tFixture) {
		for (int i = 0; i < tFixture.rowCount(); i++) {
			int totalFound = 0;
			boolean found = findRowByCriteria(tFixture, command, i, totalFound);
			if (found) {
				JTableCellFixture cell = tFixture.cell(TableCell.row(i).column(
						1));
				selectCellPopupMenuItem(command, cell);
				return String.valueOf(i);
			}
		}
		return "No row matching provided criteria !";
	}

	private void selectCellPopupMenuItem(final TableCommandRequest command,
			JTableCellFixture cell) {
		FestRobotInstance.runOutsideEDT(new Runnable() {
			@Override
			public void run() {
				cell.showPopupMenu().menuItemWithPath(command.value).click();					
			}
		});
	}

	private String handleCountAction(JTableFixture tFixture) {
		int tries = 30;
		while (tFixture.rowCount() == 0 && tries > 0) {
			try {
				tries--;
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return String.valueOf(tFixture.rowCount());
	}

	private String handleDoubleClickAction(final TableCommandRequest command,
			JTableFixture tFixture) {
		TableCommandRequest tcommand;
		tcommand = (TableCommandRequest) command;
		boolean found = false;
		for (int i = 0; i < tFixture.rowCount(); i++) {
			int totalFound = 0;
			found = findRowByCriteria(tFixture, tcommand, i, totalFound);
			if (found) {
				JTableCellFixture cell = tFixture.cell(TableCell.row(i).column(
						1));
				cell.select();
				cell.doubleClick();
				return null;
			}
		}
		return "No row matching provided criteria !";
	}

	private String handleFindAction(final TableCommandRequest command,
			JTableFixture tFixture) {
		TableCommandRequest tcommand = (TableCommandRequest) command;
		if (tcommand.query.criteria.size() == 0) {
			return "No Criteria to select a row !";
		}
		if (tFixture.rowCount() == 0) {
			return "The table is empty !";
		}
		for (int i = 0; i < tFixture.rowCount(); i++) {
			int totalFound = 0;
			boolean found = findRowByCriteria(tFixture, tcommand, i, totalFound);
			if (found) {
				if (tcommand.query.resultCol != null) {
					JTableCellFixture cell = tFixture.cell(TableCellByColumnId.row(i).columnId(tcommand.query.resultCol));
					cell.select();
					return cell.value();
				} else {
					try {
						tFixture.selectRows(i);
					} catch (Exception ex) {
					}
					return String.valueOf((i + 1));
				}
			}
		}
		return "No row matching provided criteria !";
	}

	private boolean findRowByCriteria(JTableFixture tFixture,
			TableCommandRequest tcommand, int i, int totalFound) {
		for (TableCommandRequestQueryCriteria criterion : tcommand.query.criteria) {
			JTableCellFixture cell = tFixture.cell(TableCellByColumnId.row(i)
					.columnId(criterion.lookupCol));
			if (cell.value().equals(criterion.lookupValue)) {
				totalFound++;
			}
			if (totalFound == tcommand.query.criteria.size()) {
				return true;
			}
		}
		return false;
	}
}
