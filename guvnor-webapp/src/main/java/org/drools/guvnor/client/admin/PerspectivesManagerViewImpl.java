package org.drools.guvnor.client.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.ui.*;
import org.drools.guvnor.client.common.ErrorPopup;
import org.drools.guvnor.client.messages.Constants;
import org.drools.guvnor.client.rpc.IFramePerspectiveConfiguration;
import org.drools.guvnor.client.util.SaveCommand;

import java.util.ArrayList;
import java.util.Collection;

public class PerspectivesManagerViewImpl extends Composite implements PerspectivesManagerView {

    interface PerspectivesManagerViewImplBinder
            extends
            UiBinder<Widget, PerspectivesManagerViewImpl> {
    }

    private static PerspectivesManagerViewImplBinder uiBinder = GWT.create(PerspectivesManagerViewImplBinder.class);

    private Presenter presenter;

    @UiField
    ListBox perspectivesList;

    @UiField
    Button newPerspective;

    @UiField
    Button editPerspective;

    @UiField
    Button removePerspective;

    private PerspectiveEditorPopUp perspectiveEditorPopUp;

    public PerspectivesManagerViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public String getSelectedPerspectiveUuid() {
        int selectedIndex = perspectivesList.getSelectedIndex();
        if (selectedIndex < 0) {
            return null;
        } else {
            return perspectivesList.getValue(selectedIndex);
        }
    }

    public void addPerspective(String uuid, String name) {
        perspectivesList.addItem(name, uuid);
    }

    public void openPopUp(SaveCommand<IFramePerspectiveConfiguration> saveCommand) {
        perspectiveEditorPopUp = new PerspectiveEditorPopUp(new PerspectiveEditorPopUpViewImpl());
        perspectiveEditorPopUp.show(saveCommand);
    }

    public void openPopUp(SaveCommand<IFramePerspectiveConfiguration> saveCommand, IFramePerspectiveConfiguration iFramePerspectiveConfiguration) {
        perspectiveEditorPopUp = new PerspectiveEditorPopUp(new PerspectiveEditorPopUpViewImpl());
        perspectiveEditorPopUp.setConfiguration(iFramePerspectiveConfiguration);
        perspectiveEditorPopUp.show(saveCommand);
    }

    public void closePopUp() {
        perspectiveEditorPopUp.hide();
    }

    public void removePerspective(String uuid) {
        for (int i = 0; i < perspectivesList.getItemCount(); i++) {
            if (perspectivesList.getValue(i).equals(uuid)) {
                perspectivesList.removeItem(i);
                break;
            }
        }
    }

    public Collection<String> getListOfPerspectiveNames() {
        Collection<String> result = new ArrayList<String>();
        for (int i = 0; i < perspectivesList.getItemCount(); i++) {
            result.add(perspectivesList.getItemText(i));
        }
        return result;
    }

    public void showNameTakenError(String name) {
        ErrorPopup.showMessage(Constants.INSTANCE.NameTakenForModel(name));
    }

    public void showNoSelectedPerspectiveError() {
        ErrorPopup.showMessage(Constants.INSTANCE.PleaseSelectAPerspective());
    }

    @UiHandler("newPerspective")
    public void addNewPerspective(ClickEvent event) {
        presenter.onAddNewPerspective();
    }

    @UiHandler("editPerspective")
    public void editPerspective(ClickEvent event) {
        try {
            presenter.onEditPerspective();
        } catch (SerializationException e) {
            ErrorPopup.showMessage(Constants.INSTANCE.FailedToLoadPerspective());
        }
    }

    @UiHandler("removePerspective")
    public void removePerspective(ClickEvent event) {
        presenter.onRemovePerspective();
    }
}
